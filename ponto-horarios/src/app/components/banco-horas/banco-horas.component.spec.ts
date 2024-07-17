import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BancoHorasComponent } from './banco-horas.component';

describe('BancoHorasComponent', () => {
  let component: BancoHorasComponent;
  let fixture: ComponentFixture<BancoHorasComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BancoHorasComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(BancoHorasComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
